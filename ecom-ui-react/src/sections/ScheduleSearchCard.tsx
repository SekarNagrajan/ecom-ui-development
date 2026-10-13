import { useState, type FormEvent } from 'react'
import DateField from '../components/DateField'
import Icon from '../components/Icon'
import LocationField from '../components/LocationField'
import { links } from '../config/links'

const TABS = ['Schedules', 'Tracking', 'Rates'] as const
type Tab = (typeof TABS)[number]

const DEFAULT_FROM = '2027-01-25'
const DEFAULT_TO = '2027-03-01'

export type ScheduleQuery = {
  origin: string
  destination: string
  fromDate: string
  toDate: string
}

type ScheduleSearchCardProps = {
  onSearch?: (query: ScheduleQuery) => void
}

export default function ScheduleSearchCard({ onSearch }: ScheduleSearchCardProps) {
  const [activeTab, setActiveTab] = useState<Tab>('Schedules')
  const [origin, setOrigin] = useState('')
  const [destination, setDestination] = useState('')
  const [fromDate, setFromDate] = useState(DEFAULT_FROM)
  const [toDate, setToDate] = useState(DEFAULT_TO)

  function swapLocations() {
    setOrigin(destination)
    setDestination(origin)
  }

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    onSearch?.({ origin, destination, fromDate, toDate })
  }

  return (
    <div className="flex w-full flex-col gap-[20px] rounded-[19px] bg-white p-[26px]">
      <div role="tablist" aria-label="Search type" className="flex gap-[10px] sm:gap-[14px]">
        {TABS.map((tab) => {
          const isActive = tab === activeTab
          return (
            <button
              key={tab}
              type="button"
              role="tab"
              aria-selected={isActive}
              onClick={() => setActiveTab(tab)}
              className={`flex h-[44px] min-w-0 flex-1 cursor-pointer sm:w-[170px] sm:flex-none items-center justify-center rounded-[48px] px-[10px] py-[6px] whitespace-nowrap transition focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand ${
                isActive
                  ? 'bg-brand text-[14px] text-white'
                  : 'border border-brand text-[12px] text-brand hover:bg-brand/5'
              }`}
            >
              {tab}
            </button>
          )
        })}
      </div>

      <form
        role="tabpanel"
        aria-label={activeTab}
        onSubmit={handleSubmit}
        className="flex w-full flex-col gap-[20px] xl:flex-row"
      >
        <div className="flex min-w-0 flex-1 flex-col gap-[10px] md:flex-row md:items-center">
          <LocationField id="origin" label="Origin" value={origin} onChange={setOrigin} />
          <div className="flex items-center self-center md:h-full md:self-stretch md:pt-[25px]">
            <button
              type="button"
              onClick={swapLocations}
              aria-label="Swap origin and destination"
              className="cursor-pointer rounded-full transition hover:brightness-95 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand"
            >
              <Icon name="exchange" />
            </button>
          </div>
          <LocationField
            id="destination"
            label="Destination"
            value={destination}
            onChange={setDestination}
          />
        </div>

        <div className="flex min-w-0 flex-1 flex-col gap-[10px] md:flex-row md:items-end">
          <DateField
            id="from-date"
            label="From Date"
            value={fromDate}
            isDefault={fromDate === DEFAULT_FROM}
            onChange={setFromDate}
          />
          <DateField
            id="to-date"
            label="To Date"
            value={toDate}
            min={fromDate}
            isDefault={toDate === DEFAULT_TO}
            onChange={setToDate}
          />
          <button
            type="submit"
            className="flex h-[44px] w-full shrink-0 cursor-pointer items-center justify-center rounded-[12px] bg-brand px-[10px] py-[6px] text-[12px] text-white transition hover:brightness-95 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand md:w-[266px]"
          >
            Search
          </button>
        </div>
      </form>

      <div className="flex flex-col gap-2 md:flex-row md:items-center md:justify-between">
        <p className="text-[14px] text-ink-muted">
          You'll be able to compare departure, arrival, vessel, transit time and cut-off deadlines.
        </p>
        <a
          href={links.advancedScheduleSearch}
          className="shrink-0 text-[12px] whitespace-nowrap text-brand hover:underline"
        >
          Advanced Schedule Search
        </a>
      </div>
    </div>
  )
}
